let globalAddress = "http://localhost:8081";
let currentLayer;
const toastLive = document.getElementById('liveToast');
const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLive);

function getFormData() {
    let startInputForm = document.getElementById("input-start").value;
    let targetInputForm = document.getElementById("input-ziel").value;

    //let type = document.querySelector('input[name="check-radio"]:checked').value;
    let start = parseAddress(startInputForm);
    let target = parseAddress(targetInputForm);

    if (start.error) {
        console.error("Fehler beim Parsing der Adresse:", start.error);
        printErrorOnUI(start.error);
    } else if (target.error) {
        console.error("Fehler beim Parsing der Adresse:", target.error);
        printErrorOnUI(target.error);
    } else {
        clearErrorAfterValidResponse();
        fetchGeoJsonData(start.name, start.houseNumber, target.name, target.houseNumber);
    }

}

async function fetchGeoJsonData(start, startNo, target, targetNo) {
    try {
        let url = `${globalAddress}/pathfinding?stStr=${start}&stNo=${startNo}&tgStr=${target}&tgNo=${targetNo}`;
        const response = await fetch(url);
        if (response.status === 0) {
            printErrorOnUI("API nicht erreichbar...");
        } else if (response.status === 404 || response.status === 500) {
            let err = await response.json();
            printErrorOnUI(err.error);
        }

        const geoJsonData = await response.json();
        processGeoJson(geoJsonData);

        const geoObj = geoJsonData.features;
        computeHeuristic(geoObj, start, startNo, target, targetNo);

    } catch (error) {
        console.error('Error on calling GeoJSON-data: ', error);
    }
}

function processGeoJson(geoJsonData) {
    if (geoJsonData.type === 'FeatureCollection') {
        if (currentLayer) {
            map.removeLayer(currentLayer);
        }

        currentLayer = L.geoJSON(geoJsonData.features, {
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
    } else {
        console.error('Unerwartetes GeoJSON-Format');
    }
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

    return {
        lat: lat,
        lng: lng
    }
}

function setAutomaticZoom(length) {
    if (length < 500) {
        return 18;
    } else if (1600 > length && length > 500) {
        return 16;
    } else {
        return 12;
    }
}

function parseAddress(address) {
    // Formatbeispiele: "Musterstraße 123", "Musterstraße 123A", "Musterstraße 12B"
    const regex = /^(.+?)\s+(\d+[a-zA-Z]?)$/;
    const match = address.trim().match(regex);

    if (!match) {
        return {
            error: "Invalid address. Please use format 'street number'."
        };
    }

    const name = match[1].trim();
    const houseNumber = match[2].trim();

    return {
        name,
        houseNumber
    };
}

async function fetchStreetListFromApi() {
    try {
        let url = `${globalAddress}/streets/list`;
        const response = await fetch(url);
        if (!response.ok) {
            console.log("API nicht erreichbar...");
        }

        const data = await response.json();
        let table = document.getElementById("streetList");
        for (let street of data) {
            table.innerHTML = table.innerHTML + buildListElement(street.id, street.name, street.children.length);
        }
    } catch (error) {
        console.error('Error on calling GeoJSON-data: ', error);
    }
}

async function fetchAllBuildingsFromApi() {
    try {
        let url = `${globalAddress}/streets/objects`;
        const response = await fetch(url);
        if (!response.ok) {
            console.log("API nicht erreichbar...");
        }

        const data = await response.json();
        let table = document.getElementById("table-body");
        let count = 1;
        for (let street of data) {
            table.innerHTML = table.innerHTML + buildTableElement(count, street);
            count++;
        }
    } catch (error) {
        console.error('Error on calling GeoJSON-data: ', error);
    }
}

function buildListElement(streetId, name, childElements) {
    return `<li class='list-group-item d-flex justify-content-between align-items-center list-group-item-dark'>
            Id=${streetId}: ${name}
            <span class='badge text-bg-primary rounded-pill'>${childElements}</span>
        </li>`;
}

function buildTableElement(count, street) {
    return `<tr><td>${count}</td><td>${street.id}</td><td>${street.name}</td><td>${street.houseNumber}</td></td></tr>`;
}

function printErrorOnUI(message) {
    let errorField = document.getElementById("errorMsg");
    errorField.innerHTML = message;
    errorField.setAttribute("class", "alert alert-danger text-alert");
    errorField.setAttribute("style", "visibility: visible");
    console.log("Err: " + message);

    let errorToastElement = document.getElementById("errorMsgToast");
    errorToastElement.innerHTML = message;
    toastBootstrap.show();
}

function clearErrorAfterValidResponse() {
    let errorField = document.getElementById("errorMsg");
    errorField.innerHTML = "";
    errorField.setAttribute("class", "");
    errorField.setAttribute("style", "visibility: hidden");

    let errorToastElement = document.getElementById("errorMsgToast");
    errorToastElement.innerHTML = "";
}