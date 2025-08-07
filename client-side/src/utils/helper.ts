export const isUserAuthorized = () => {
    const token = !!localStorage.getItem("jwt")
    const username = !!localStorage.getItem("username")
    const role = !!localStorage.getItem("role")
    return token && username && role
}

export function getAuthToken() {
    return localStorage.getItem("jwt") || ""
}

export function setAuthToken(token: string) {
    localStorage.setItem("jwt", token)
}

export function getUsername() {
    return localStorage.getItem("username") || ""
}

export function setUsername(username: string) {
    return localStorage.setItem("username", username)
}

export function getRole() {
    return localStorage.getItem("role") || ""
}

export function setRole(role: string) {
    return localStorage.setItem("role", role)
}

export function clearAuthToken() {
    localStorage.removeItem("jwt")
    localStorage.removeItem("username")
    localStorage.removeItem("role")
}