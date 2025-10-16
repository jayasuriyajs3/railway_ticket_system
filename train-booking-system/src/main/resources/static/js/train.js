document.addEventListener('DOMContentLoaded', () => {
    // Selects all elements with the class 'delete-btn'
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            // Displays a confirmation box before navigation
            if (!confirm('Are you sure you want to permanently delete this train route?')) {
                e.preventDefault(); // Stops the link from being followed
            }
        });
    });
});