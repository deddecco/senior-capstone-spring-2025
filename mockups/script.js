let folders = {};
let sightings = [];

function createFolder() {
    let folderName = document.getElementById('folderName').value.trim();
    if (folderName && !Object.hasOwn(folders, folderName)) {
        folders[folderName] = [];
        updateFolderDropdowns();
        displayFolders();
        document.getElementById('folderName').value = '';
    }
}

function updateFolderDropdowns() {
    let folderSelect = document.getElementById('folderSelect');
    let filterFolder = document.getElementById('filterFolder');
    folderSelect.innerHTML = '';
    filterFolder.innerHTML = '<option value="all">All Folders</option>';

    Object.keys(folders).forEach(folder => {
        folderSelect.innerHTML += `<option value="${folder}">${folder}</option>`;
        filterFolder.innerHTML += `<option value="${folder}">${folder}</option>`;
    });
}

function displayFolders() {
    let folderDiv = document.getElementById('folders');
    folderDiv.innerHTML = '';
    Object.keys(folders).forEach(folder => {
        folderDiv.innerHTML += `<div class="folder"><strong>${folder}</strong></div>`;
    });
}

function addSighting() {
    let species = document.getElementById('species').value.trim();
    let location = document.getElementById('location').value.trim();
    let date = document.getElementById('date').value;
    let folder = document.getElementById('folderSelect').value;

    if (species && location && date && folder) {
        let sighting = {species, location, date, folder};
        sightings.push(sighting);
        folders[folder].push(sighting);
        displaySightings();
        document.getElementById('species').value = '';
        document.getElementById('location').value = '';
        document.getElementById('date').value = '';
    }
}

function displaySightings() {
    let sightingsDiv = document.getElementById('sightings');
    sightingsDiv.innerHTML = '';
    let filter = document.getElementById('filterFolder').value;
    let filteredSightings = filter === 'all' ? sightings : folders[filter];

    filteredSightings.forEach(sighting => {
        sightingsDiv.innerHTML += `
            <div class="sighting-card">
                <strong>${sighting.species}</strong> seen at <em>${sighting.location}</em> on ${sighting.date}
                <br><a href="https://en.wikipedia.org/wiki/${sighting.species}" target="_blank">Learn More</a>
            </div>
        `;
    });
}

function filterSightings() {
    displaySightings();
}

updateFolderDropdowns();