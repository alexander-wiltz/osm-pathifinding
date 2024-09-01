function getFormData() {
    let start = document.getElementById("input-start").value;
    let target = document.getElementById("input-ziel").value;
    let type = document.querySelector('input[name="check-radio"]:checked').value;

    try {
        start = start.toLowerCase().replace(" ", "+");
        target = target.toLowerCase().replace(" ", "+");
    } catch (error) {
        return;
    }

    callApis(start, target, type).then(r =>
        function() {}
    );

}

async function callApis(start, target, type) {
    console.log("Asynchronous loading.");
    await getComputedWayFromApi(start, target, type);
    await getComputedWayFromApiClearText(start, target, type);
}

function getComputedWayFromApi(start, target, type) {
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

    let url = `http://localhost:8081/api/v1/way?start=${start}&target=${target}&type=${type}`;

    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function getComputedWayFromApiClearText(start, target, type) {
    console.log("Response: Clear way...")
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