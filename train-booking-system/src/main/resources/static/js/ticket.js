document.addEventListener('DOMContentLoaded', () => {
    // Confirmation for cancelling/deleting a ticket
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            if (!confirm('Are you sure you want to CANCEL this ticket? This action may be irreversible.')) {
                e.preventDefault();
            }
        });
    });

    // Optional: Call calculatePrice on load for 'Edit' forms if a train is already selected
    const priceInput = document.getElementById('price');
    if (priceInput && priceInput.value === "") {
        const trainSelect = document.getElementById('trainSelect');
        if (trainSelect && trainSelect.value !== "") {
             // Delay slightly to ensure all DOM is ready
            setTimeout(() => calculatePrice(), 50); 
        }
    }
});