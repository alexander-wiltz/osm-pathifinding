// Startview
// TODO Zentrum muss ermittelt werden über euklidische Distanz zwischen Start und Ziel

var map = L.map('map').setView([49.235812, 6.706272], 16);

// basic rendering
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

// PopUp
function onMapClick(e) {
    L.popup().setLatLng(e.latlng).setContent("Point: " + e.latlng.toString()).openOn(map);
}

map.on('click', onMapClick);

