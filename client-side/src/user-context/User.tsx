import React, { useCallback, useContext, useEffect, useState } from "react";
import { clearAuthToken, getAuthToken } from "../utils/helper";
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

export function useUserSetter() {
    const { setUser } = React.useContext(UserContext)
    const setter = useCallback(
        (username: string | undefined, role: string | undefined, loggedIn: boolean) => {
            if (setUser) setUser({ username: username, role: role, isLoggedIn: loggedIn })
        },
        [setUser]
    )

    return setter
}

export function useUsername() {
    const { user } = React.useContext(UserContext)
    return user.username
}

export function useRole() {
    const { user } = useContext(UserContext)
    return user.role
}

export function isLoggedIn() {
    const { user } = React.useContext(UserContext);
    return !!user.isLoggedIn;
}

export function useUser() {
    const context = useContext(UserContext)

    const logout = () => {
        clearAuthToken()
        context.setUser?.({ isLoggedIn: false })
    }

    return {
        ...context,
        logout
    }
}