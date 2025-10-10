const form = document.getElementById("data-form");
const resultTable = document.getElementById("result-table");
const errorMsg = document.getElementById("error");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const x = document.getElementById("x").value;
    const y = document.getElementById("y").value;
    const r = document.getElementById("r").value;

    const point = {x, y, r};

    try {
        errorMsg.hidden = true;

        const response = await fetch("controller", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(point),
        });

        if (!response.ok) {
            const data = await response.json();
            throw new Error(data?.error ?? "Unknown error");
        }

        // data = await response.json();
    } catch (error) {
        // console.error(error);
        errorMsg.textContent = "Error! " + (error);
        errorMsg.hidden = false;
    }
});
