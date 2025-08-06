import React, { useContext, useEffect, useState } from "react";
import { getAuthToken } from "../utils/helper";
import { getUsername } from "../utils/helper";
import { getRole } from "../utils/helper";

export interface User {
    username?: string
    role?: string
    isLoggedIn?: boolean
}

const UserContext = React.createContext<{
    user: User
    setUser?: React.Dispatch<React.SetStateAction<User>>
}>({
    user: { isLoggedIn: false },
    setUser: () => { }
})

export function UserProvider({ children }: React.PropsWithChildren) {
    const [user, setUser] = useState<User>({ isLoggedIn: false })

    useEffect(() => {
        const token = getAuthToken()
        const username = getUsername()
        const role = getRole()

        if (token && username && role) {
            setUser({ username, role, isLoggedIn: true })
        }
    }, [])

    return (
        <UserContext.Provider value={{ user, setUser }}>
            {children}
        </UserContext.Provider>
    )
}

export function useUser() {
    return useContext(UserContext)
}