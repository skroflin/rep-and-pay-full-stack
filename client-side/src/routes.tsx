import { Navigate, Outlet } from "react-router"
import { useUser } from "./user/User"

export interface RouteElement {
    key: string
    path: string
    element: React.JSX.Element
    onNavBar: boolean
    reqLogin: boolean
}

const routes: RouteElement[] = [
    {
        key: "SignUp",
        path: "/sign-up",
        element: <SignUp/>,
        onNavBar: false,
        reqLogin:
    },
    {
        key: "LogIn",
        path: "/log-in",
        element: <LogIn/>,
        onNavBar: false,
        reqLogin:
    }
]

interface PrivateRouteProps {
    reqLogin: boolean
}

function PrivateRoute({ reqLogin }: PrivateRouteProps){
    const { user } = useUser()
    const isUserLoggedIn = !!user.isLoggedIn

    return !reqLogin || isUserLoggedIn ? <Outlet /> : <Navigate to="/login" />
}