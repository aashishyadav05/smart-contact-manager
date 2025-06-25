function toggleSidebar() {
  const sidebar = document.getElementById("sidebar");
  const content = document.querySelector(".content");
  const toggleIcon = document.querySelector("#toggleBtn i");

  sidebar.classList.toggle("sidebar-collapsed");
  content.classList.toggle("content-collapsed");

  // Change icon
  if (sidebar.classList.contains("sidebar-collapsed")) {
    toggleIcon.classList.remove("fa-times");
    toggleIcon.classList.add("fa-bars");
  } else {
    toggleIcon.classList.remove("fa-bars");
    toggleIcon.classList.add("fa-times");
  }
}
/*
document.addEventListener("DOMContentLoaded", function () {
  var alertBox = document.getElementById('alertMessage');
  if (alertBox) {
    setTimeout(function () {
      // Bootstrap 4 alert close using jQuery
      $(alertBox).alert('close');
    }, 3000); // Auto dismiss after 3 seconds
  }
});
*/

const search = () => {
	let query = $("#search-input").val();
	console.log("Query:", query);

	if (query === '') {
		$(".search-result").hide();  // hide if empty
	} else {
		let url = `http://localhost:8080/search/${query}`;

		fetch(url)
			.then(response => response.json())
			.then(data => {
				console.log("Results:", data);

				let text = `<div class='list-group'>`;

				data.forEach(contact => {
					text += `<a href='/user/contact/${contact.cID}' class='list-group-item list-group-item-action'>${contact.name}</a>`;
				});

				text += `</div>`;  // corrected closing tag

				$(".search-result").html(text).show();  // update and show
			})
			.catch(error => {
				console.error("Search error:", error);
				$(".search-result").html("<p class='text-danger'>Error loading results</p>").show();
			});
	}
};
 
function openCardModal(title, content, showUpdate = false) {
    document.getElementById('modalLabel').innerText = title;
    document.getElementById('modalContent').innerText = content;

    // Show or hide Update button based on third argument
    const updateBtn = document.getElementById('updateBtn');
    if (showUpdate) {
      updateBtn.classList.remove('d-none');
    } else {
      updateBtn.classList.add('d-none');
    }

    $('#infoModal').modal('show');
  }

  
  document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    const shouldOpenModal = body.getAttribute("data-modal-open") === "true";

    if (shouldOpenModal) {
      // Use jQuery if using Bootstrap 4
      $('#resetPasswordModal').modal('show');
    }
  });

  document.addEventListener("DOMContentLoaded", function () {
    const modalFlag = document.body.getAttribute("data-modal-open") === "true";

    if (modalFlag) {
      $('#resetPasswordModal').modal('show');
    }
  });
  
  document.addEventListener("DOMContentLoaded", function () {
    const shouldOpenModal = document.body.getAttribute("data-modal-open") === "true";

    if (shouldOpenModal) {
      console.log("Opening modal...");
      $('#resetPasswordModal').modal('show');
    }
  });














