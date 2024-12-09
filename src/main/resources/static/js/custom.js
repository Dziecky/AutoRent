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

(function () {
    'use strict'

    // Pobierz wszystkie formularze, które mają klasę 'needs-validation'
    var forms = document.querySelectorAll('.needs-validation')

    // Pętla po każdym formularzu i dodaj nasłuchiwanie na submit
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
})()