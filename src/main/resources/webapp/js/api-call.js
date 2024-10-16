let globalAddress = "http://localhost:8081";
let geoLayer;

function getFormData() {
    let startInputForm = document.getElementById("input-start").value;
    let targetInputForm = document.getElementById("input-ziel").value;

    //let type = document.querySelector('input[name="check-radio"]:checked').value;

    let start = parseAddress(startInputForm);
    let target = parseAddress(targetInputForm);

    if (start.error) {
        console.error("Fehler beim Parsing der Adresse:", start.error);
        alert(`Die eingegebene Adresse ist ungültig: ${start.error}`);
    } else if (target.error) {
        console.error("Fehler beim Parsing der Adresse:", target.error);
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

            geoLayer = L.geoJSON(geoObj, {
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
            }).addTo(map);

            computeHeuristic(geoObj, start, startNo, target, targetNo);

        } else if (xmlhttp.readyState === 4 && xmlhttp.status === 0) {
            console.log("API nicht erreichbar...");
        } else if (xmlhttp.readyState === 4 && (xmlhttp.status === 404 || xmlhttp.status === 500)) {
            // Not Found or Bad Request → Errorhandling von Backend
            let err = JSON.parse(xmlhttp.responseText);
            console.log(err);
            console.log("Msg: " + err.error);
        } else {

        }
    }

    let url = `${globalAddress}/pathfinding?stStr=${start}&stNo=${startNo}&tgStr=${target}&tgNo=${targetNo}`;

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

/**
 *
 *
 * @param geoObj
 * @param startStr
 * @param startNo
 * @param targetStr
 * @param targetNo
 */
function computeHeuristic(geoObj, startStr, startNo, targetStr, targetNo) {
    let start;
    let target;

    for (let feature of geoObj) {
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

    // set new center after calculating the way
    let midPoint = findCenter(startLatLng, targetLatLng);
    let zoom = setAutomaticZoom(heuristic);
    map.setView([midPoint.lng, midPoint.lat], zoom);

    console.log(`Luftlinie ${heuristic}m von ${startStr} ${startNo} nach ${targetStr} ${targetNo}`);
    console.log(`Set zoom ${zoom}`);
}

function findCenter(...markers) {
    let lat = 0;
    let lng = 0;

    for (let i = 0; i < markers.length; ++i) {
        lat += markers[i].lat;
        lng += markers[i].lng;
    }

    lat /= markers.length;
    lng /= markers.length;

    return {lat: lat, lng: lng}
}

function setAutomaticZoom(length) {
    if (length < 500) {
        return 18;
    } else if (1000 > length && length > 500) {
        return 16;
    } else if (1500 > length && length > 1000) {
        return 14;
    } else {
        return 10;
    }
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

function getStreetListFromApi() {
    let xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = JSON.parse(xmlhttp.responseText);

            let table = document.getElementById("streetList");
            for (street of response) {
                table.innerHTML = table.innerHTML + buildListElement(street.id, street.street, street.children.length);
            }

        } else if (xmlhttp.readyState === 4 && xmlhttp.status === 0) {
            console.log("API nicht erreichbar...");
        } else if (xmlhttp.readyState === 4 && xmlhttp.status === 500) {
            // Bad Request → Errorhandling von Backend
            let err = JSON.parse(xmlhttp.responseText);
            console.log(err);
        } else {
            // Cleanup on ERROR
        }
    }

    let url = `${globalAddress}/streets/list`;

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function buildListElement(streetId, street, childElements) {
    return `<li class='list-group-item d-flex justify-content-between align-items-center list-group-item-dark'>
            Id=${streetId}: ${street}
            <span class='badge text-bg-primary rounded-pill'>${childElements}</span>
        </li>`;
}