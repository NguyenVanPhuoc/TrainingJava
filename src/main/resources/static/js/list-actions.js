// Common functions for list pages (users, stores, products, orders)
document.addEventListener('DOMContentLoaded', function() {
    const selectAll = document.getElementById('selectAll');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
    const selectedItemIds = document.getElementById('selectedItemIds');

    if (itemCheckboxes.length > 0) {
        // Handle "Select All" checkbox
        selectAll.addEventListener('change', function() {
            console.log('Select All changed:', this.checked);
            itemCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateDeleteButton();
        });

        // Handle individual checkboxes
        itemCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                console.log('Checkbox changed:', this.value, this.checked);
                updateDeleteButton();
            });
        });

        // Update delete button state
        function updateDeleteButton() {
            const checkedBoxes = document.querySelectorAll('.item-checkbox:checked');
            console.log('Checked boxes:', checkedBoxes.length);
            deleteSelectedBtn.disabled = checkedBoxes.length === 0;
			selectAll.checked = checkedBoxes.length === itemCheckboxes.length;
        }

        // Handle delete selected button
        deleteSelectedBtn.addEventListener('click', function() {
            console.log('Delete Selected clicked');
            const checkedBoxes = document.querySelectorAll('.item-checkbox:checked');
            const itemIds = Array.from(checkedBoxes).map(cb => cb.value);
            selectedItemIds.value = itemIds.join(',');
            
            const deleteModal = document.getElementById('deleteMultipleItemsModal');
            if (deleteModal) {
                const form = deleteModal.querySelector('form');
                const input = form.querySelector('input[name="itemIds"]');
                if (input) {
                    input.value = itemIds.join(',');
                    console.log('Setting selected IDs in form:', input.value);
                }
                new bootstrap.Modal(deleteModal).show();
            } else {
                console.error('Delete modal not found!');
            }
        });
    }

    // Handle delete button for individual items
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const itemId = this.dataset.itemId;
            console.log('Delete button clicked, data-item-id:', itemId);
            console.log('Button dataset:', this.dataset);
            
            if (!itemId) {
                console.error('No ID found for delete button');
                return;
            }

            const modal = document.getElementById('deleteItemModal');
            console.log('Found modal:', modal);
            
            if (modal) {
                const itemIdSpan = modal.querySelector('#deleteItemId');
                console.log('Found itemIdSpan:', itemIdSpan);
                
                if (itemIdSpan) {
                    itemIdSpan.textContent = itemId;
                    console.log('Updated item ID in modal:', itemId);
                }
                
                const form = modal.querySelector('form');
                console.log('Found form:', form);
                if (form) {
                    // Update form action to include the ID in the URL path
                    form.action = form.action + itemId;
                    console.log('Updated form action:', form.action);
                }
                
                new bootstrap.Modal(modal).show();
            } else {
                console.error('Delete modal not found!');
            }
        });
    });
});
