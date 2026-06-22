document.addEventListener("DOMContentLoaded", () => {

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

            const darkEnabled =
                document.documentElement.classList.contains("dark-mode");

            if(darkEnabled){

                localStorage.setItem("theme","dark");
                toggle.textContent = "☀️";

            }else{

                localStorage.setItem("theme","light");
                toggle.textContent = "🌙";

            }
        });
    }

});