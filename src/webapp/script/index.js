const form = document.getElementById("data-form");
const errorMsg = document.getElementById("error");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const x = document.getElementById("x").value;
    const y = document.getElementById("y").value;
    const r = document.getElementById("r").value;
    const point = {x, y, r};

    await sendRequest(point);
});

async function sendRequest(point) {
    try {
        errorMsg.hidden = true;

        const response = await fetch("controller", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            redirect: "follow",
            credentials: "same-origin",
            body: JSON.stringify(point),
        });

        if (response.redirected) {
            console.log("redirect");
            console.log(response.url);
            window.location.href = response.url;
            return;
        }

        if (!response.ok) {
            const data = await response.json();
            throw new Error(data?.error ?? "Unknown error");
        }
    } catch (error) {
        // console.error(error);
        errorMsg.textContent = "Error! " + (error);
        errorMsg.hidden = false;
    }
}