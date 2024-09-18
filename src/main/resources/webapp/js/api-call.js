function getFormData() {
    let startInputForm = document.getElementById("input-start").value;
    let targetInputForm = document.getElementById("input-ziel").value;

    //let type = document.querySelector('input[name="check-radio"]:checked').value;

    let start = parseAddress(startInputForm);
    let target = parseAddress(targetInputForm);

    if (start.error) {
        console.error("Fehler beim Adress-Parsing:", start.error);
        alert(`Die eingegebene Adresse ist ungültig: ${start.error}`);
    } else if (target.error) {
        console.error("Fehler beim Adress-Parsing:", target.error);
        alert(`Die eingegebene Adresse ist ungültig: ${target.error}`);
    } else {
        getComputedWayFromApi(start.street, start.houseNumber, target.street, target.houseNumber);
    }

}

function getComputedWayFromApi(start, startNo, target, targetNo) {
    let xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let way = JSON.parse(xmlhttp.responseText);
            let geoObj = way.features;

            L.geoJSON(geoObj, {
                style: function (feature) {
                    switch (feature.properties.name) {
                        case 'Route':
                            return {
                                color: "#ff7800",
                                weight: 3,
                                opacity: 0.75
                            };
                    }
                }
            }).bindPopup(function (layer) {
                return layer.feature.properties.name;
            }).addTo(map);

            computeHeuristic(geoObj);

        } else if (xmlhttp.readyState === 4 && xmlhttp.status === 0) {
            console.log("API nicht erreichbar...");
        } else {
            // Cleanup on ERROR
        }
    }

    /**
     * @PathParam("stStr") String startStreet
     * @PathParam("stNo") String startNumber
     * @PathParam("tgStr") String targetStreet
     * @PathParam("tgNo") String targetNumber
     */
    let url = `http://localhost:8081/pathfinding?stStr=${start}&stNo=${startNo}&tgStr=${target}&tgNo=${targetNo}`;

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

/**
 *
 *
 * @param features
 */
function computeHeuristic(features) {
    let start;
    let target;

    for (let feature of features) {
        if (feature.properties.name === "Start") {
            start = feature.geometry.coordinates;
        }
        if (feature.properties.name === "Ziel") {
            target = feature.geometry.coordinates;
        }
    }

    let startLatLng = L.latLng(start[0], start[1]);
    let targetLatLng = L.latLng(target[0], target[1]);
    let heuristic = startLatLng.distanceTo(targetLatLng).toFixed(3);
    console.log("Luftlinie " + heuristic + "m");
}

function parseAddress(address) {
    // Regulärer Ausdruck zum Parsen der Adresse (Straße und Hausnummer)
    // Formatbeispiele: "Musterstraße 123", "Musterstraße 123A", "Musterstraße 12B"
    const regex = /^(.+?)\s+(\d+[a-zA-Z]?)$/;
    const match = address.trim().match(regex);

    if (!match) {
        return {
            error: "Ungültige Adresse. Bitte im Format 'Straßenname Hausnummer' eingeben."
        };
    }

    const street = match[1].trim();
    const houseNumber = match[2].trim();

    return {
        street,
        houseNumber
    };
}