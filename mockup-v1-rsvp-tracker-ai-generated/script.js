document.addEventListener("DOMContentLoaded", () => {
    const guestList = document.getElementById("guest-list");
    const sortSelect = document.getElementById("sort");

    let guests = [
        { name: "Alice", response: "Attending" },
        { name: "Bob", response: "Not Attending" },
        { name: "Charlie", response: "No Response" },
        { name: "David", response: "Invited" }
    ];

    const responseClasses = {
        "Invited": "invited",
        "Attending": "attending",
        "Not Attending": "not-attending",
        "No Response": "no-response"
    };

    function renderGuests() {
        guestList.innerHTML = "";
        guests.forEach(guest => {
            const guestCard = document.createElement("div");
            guestCard.className = `guest-card ${responseClasses[guest.response]}`;
            guestCard.innerHTML = `<strong>${guest.name}</strong><br>
                                   <em>${guest.response}</em>`;
            guestList.appendChild(guestCard);
        });
    }

    sortSelect.addEventListener("change", (e) => {
        const sortBy = e.target.value;
        if (sortBy === "name") {
            guests.sort((a, b) => a.name.localeCompare(b.name));
        } else if (sortBy === "response") {
            guests.sort((a, b) => a.response.localeCompare(b.response));
        }
        renderGuests();
    });

    renderGuests();
});
