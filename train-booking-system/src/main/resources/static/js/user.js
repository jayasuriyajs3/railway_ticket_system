document.addEventListener("DOMContentLoaded", () => {
    // Selects all elements with the class 'delete-btn'
    const deleteButtons = document.querySelectorAll(".delete-btn");

    deleteButtons.forEach(btn => {
        btn.addEventListener("click", (e) => {
            // Displays a confirmation box before navigation
            if (!confirm("Are you sure you want to delete this user? This action cannot be undone.")) {
                e.preventDefault(); // Stops the link from being followed
            }
        });
    });
});