

export async function loginApi(payload) {
    try {
        const res = await fetch("http://localhost:8080/sign-in", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });
        if (!res.ok) {
           return await res.text();
        }
        let data;    
        data = await res.text(); 
        document.cookie = `token=${data}; path=/; max-age=${60 * 60 * 24}`;
        return "Login in successfull";

    } catch (networkError) {
        return { success: false, message: "Cannot connect to server. Please try again." };
    }
};

export const registerApi = async (payload) => {
    const res = await fetch("http://localhost:8080/sign-up", {
        method: "POST",
        headers: { "Content-Type": "application/json", "x-api-key": "reqres-free-v1" },
        body: JSON.stringify(payload),
    });
    if (!res.ok) {
        const data = await res.text();
        return data.slice(0, 80);
    }
    const data = await res.json();
    console.log(data);
    return `Profile saved with username ${data.userName} and email ${data.userEmail}`;
}

export const testApi = async () => {
    try {
        const res = await fetch("http://localhost:8080/test", { method: "GET" });
        if (!res.ok) {
            return `Unexpected error (${res.status})`;
        }
        try {
            return await res.text();
        } catch (jsonError) {
            return "Invalid response from server";
        }
    } catch (networkError) {
        return "Cannot connect to server. Please try again.";
    }
}