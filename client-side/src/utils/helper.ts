export const isUserAuthorized = () => {
    const token = localStorage.getItem("jwt")
    const role = localStorage.getItem("role")
    const username = localStorage.getItem("username")
    return !!token && !!role && !!username
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