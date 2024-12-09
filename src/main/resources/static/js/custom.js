document.addEventListener("DOMContentLoaded", function () {
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
    dropdownElementList.forEach(function (dropdownToggleEl) {
        var dropdown = new bootstrap.Dropdown(dropdownToggleEl, {
            popperConfig: {
                // Możesz dodać konfigurację Poppera, jeśli potrzebne
            }
        });

        // Znajdź rodzica elementu dropdown
        var dropdownParent = dropdownToggleEl.parentElement;

        dropdownParent.addEventListener('mouseenter', function () {
            dropdown.show();
        });

        dropdownParent.addEventListener('mouseleave', function () {
            dropdown.hide();
        });
    });
});