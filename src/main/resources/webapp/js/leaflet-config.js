var map = L.map('map').setView([49.235812, 6.706272], 16);

// https://blog.selfhtml.org/2019/jan/13/einstieg-in-leaflet

// basic rendering
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 20,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);