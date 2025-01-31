document.addEventListener("DOMContentLoaded", () => {
    const sightingList = document.getElementById("sighting-list");
    const sortSelect = document.getElementById("sort");

    let sightings = [
        {species: "Bald Eagle", date: "2024-02-01", location: "Yellowstone National Park"},
        {species: "Great Horned Owl", date: "2024-01-25", location: "Appalachian Trail"},
        {species: "Snowy Egret", date: "2024-01-20", location: "Everglades National Park"},
        {species: "Peregrine Falcon", date: "2024-01-15", location: "Grand Canyon"}
    ];

    function getWikipediaLink(species) {
        return `https://en.wikipedia.org/wiki/${species.replace(/ /g, "_")}`;
    }

    function renderSightings() {
        sightingList.innerHTML = "";
        sightings.forEach(sighting => {
            const sightingCard = document.createElement("div");
            sightingCard.className = "sighting-card";
            sightingCard.innerHTML = `<strong>${sighting.species}</strong><br>
                                      <small>${sighting.date}</small><br>
                                      <em>${sighting.location}</em><br>
                                      <a href="${getWikipediaLink(sighting.species)}" target="_blank">Learn More</a>`;
            sightingList.appendChild(sightingCard);
        });
    }

    sortSelect.addEventListener("change", (e) => {
        const sortBy = e.target.value;
        if (sortBy === "date") {
            sightings.sort((a, b) => new Date(b.date) - new Date(a.date));
        } else if (sortBy === "species") {
            sightings.sort((a, b) => a.species.localeCompare(b.species));
        } else if (sortBy === "location") {
            sightings.sort((a, b) => a.location.localeCompare(b.location));
        }
        renderSightings();
    });

    renderSightings();
});