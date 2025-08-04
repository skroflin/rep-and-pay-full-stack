export const isUserAuthorized = () => {
    const token = localStorage.getItem("jwt")
    return !!token
}

export function getAuthToken() {
    return localStorage.getItem("jwt") || ""
}

export function setAuthToken(token: string) {
    localStorage.setItem("jwt", token)
}

export function clearAuthToken() {
    localStorage.removeItem("jwt")
}