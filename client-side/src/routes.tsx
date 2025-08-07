import { Navigate, Outlet, Route, Routes } from "react-router"
import { useUser } from "./user-context/User"
import { Layout } from "antd"
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import LoginIn from "./pages/LogInPage/component";
import SignUp from "./pages/SignUpPage/component";
import NavBar from "./misc/NavBar/component";
import HomePage from "./pages/HomePage/component";
import CoachPage from "./pages/CoachPage/component";
import UserPage from "./pages/UserPage/component";

export interface RouteElement {
    key: string
    path: string
    element: React.JSX.Element
    onNavBar: boolean
    reqLogin: boolean
    reqSuperUser: boolean
}

const routes: RouteElement[] = [
    {
        key: "SignUp",
        path: "/sign-up",
        element: <SignUp />,
        onNavBar: false,
        reqLogin: false,
        reqSuperUser: false
    },
    {
        key: "LogIn",
        path: "/log-in",
        element: <LoginIn />,
        onNavBar: false,
        reqLogin: false,
        reqSuperUser: false
    },
    {
        key: "Home",
        path: "/home",
        element: <HomePage />,
        onNavBar: true,
        reqLogin: true,
        reqSuperUser: true
    },
    {
        key: "Coaches",
        path: "/coaches",
        element: <CoachPage />,
        onNavBar: true,
        reqLogin: true,
        reqSuperUser: true
    },
    {
        key: "Users",
        path: "/users",
        element: <UserPage />,
        onNavBar: true,
        reqLogin: true,
        reqSuperUser: true
    }
]

interface PrivateRouteProps {
    reqLogin: boolean
}

function PrivateRoute({ reqLogin }: PrivateRouteProps) {
    const { user } = useUser()
    const isUserLoggedIn = !!user.isLoggedIn

    return !reqLogin || isUserLoggedIn ? <Outlet /> : <Navigate to="/log-in" />
}

export function AllRoutes() {
    if (routes.length === 0) return null

    return (
        <Layout
            style={{
                minHeight: 700,
                padding: "1em 0em",
                textAlign: "center"
            }}
        >
            <NavBar routes={routes} />
            <Routes>
                <Route path="/" element={<Navigate to="/log-in" replace />} />
                {routes.map((route) => (
                    <Route
                        key={route.key}
                        element={<PrivateRoute reqLogin={route.reqLogin} />}
                    >
                        <Route path={route.path} element={route.element} />
                    </Route>
                ))}
                <Route path="*" element={<Navigate to="/log-in" replace />} />
            </Routes>
            <ToastContainer
                position="top-left"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="colored"
            />
        </Layout>
    )
}