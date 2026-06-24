document.addEventListener("DOMContentLoaded", () => {

//========================= DARK MODE CODE =========================

    const toggle = document.getElementById("themeToggle");
    const isDark = localStorage.getItem("theme") === "dark";

    if(isDark){
        document.documentElement.classList.add("dark-mode");

        if(toggle){
            toggle.textContent = "☀️";
        }
    }

    if(toggle){

        toggle.addEventListener("click", () => {
            document.documentElement.classList.toggle("dark-mode");
            const darkEnabled =document.documentElement.classList.contains("dark-mode");
            if(darkEnabled){
                localStorage.setItem("theme","dark");
                toggle.textContent = "☀️";
            }else{
                localStorage.setItem("theme","light");
                toggle.textContent = "🌙";
            }
        });
    }

    // ================= MOBILE MENU CODE =================

    const menuToggle =document.getElementById("menuToggle");
    const mobileMenu =document.getElementById("mobileMenu");

    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener("click", () => {

            mobileMenu.classList.toggle("show");

        });

    }

    const menuLinks = document.querySelectorAll(".topmenu");

    menuLinks.forEach(link => {
    link.addEventListener("click", () => {
        if (mobileMenu) {
            mobileMenu.classList.remove("show");
        }

        });
    }); 

});

