document.addEventListener("DOMContentLoaded", () => {
    const jobList = document.getElementById("job-list");
    const sortSelect = document.getElementById("sort");

    let jobs = [
        { id: 1, company: "Company A", position: "Software Engineer", date: "2024-02-01", status: "Resume Sent" },
        { id: 2, company: "Company B", position: "Data Analyst", date: "2024-01-25", status: "Interview Scheduled" },
        { id: 3, company: "Company C", position: "Web Developer", date: "2024-01-20", status: "Accepted Offer" },
        { id: 4, company: "Company D", position: "Cybersecurity Specialist", date: "2024-01-15", status: "Rejected" }
    ];

    const statusClasses = {
        "Resume Sent": "resume-sent",
        "Interview Scheduled": "interview-scheduled",
        "Accepted Offer": "accepted-offer",
        "Rejected": "rejected"
    };

    function renderJobs() {
        jobList.innerHTML = "";
        jobs.forEach(job => {
            const jobCard = document.createElement("div");
            jobCard.className = `job-card ${statusClasses[job.status]}`;
            jobCard.innerHTML = `<strong>${job.company}</strong> - ${job.position}<br>
                                 <small>${job.date}</small><br>
                                 <em>${job.status}</em>`;
            jobList.appendChild(jobCard);
        });
    }

    sortSelect.addEventListener("change", (e) => {
        const sortBy = e.target.value;
        if (sortBy === "date") {
            jobs.sort((a, b) => new Date(b.date) - new Date(a.date));
        } else if (sortBy === "company") {
            jobs.sort((a, b) => a.company.localeCompare(b.company));
        } else if (sortBy === "status") {
            jobs.sort((a, b) => a.status.localeCompare(b.status));
        }
        renderJobs();
    });

    renderJobs();
});
