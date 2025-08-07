import { Navigate, Outlet, Route, Routes } from "react-router"
import { isLoggedIn, useSuperUserRole } from "./user-context/User"
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
        path: "/",
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
    reqLogin: boolean,
    reqSuperUser: boolean
}

function PrivateRoute({ reqLogin, reqSuperUser }: PrivateRouteProps) {
    const isUserLoggedIn = isLoggedIn()
    const isSuperUser = useSuperUserRole()

    if (reqLogin && !isUserLoggedIn) {
        return <Navigate to="/log-in" />
    }

    if (reqSuperUser && !isSuperUser) {
        return <Navigate to="/" />
    }

    return <Outlet />
}

export function AllRoutes() {
    if (routes.length === 0) return null
    const isUserLoggedIn = isLoggedIn()

    return (
        <Layout
            style={{
                minHeight: 700,
                paddingTop: "1em 0em",
                textAlign: "center",
            }}
        >
            {isUserLoggedIn && <NavBar routes={routes} />}
            <Routes>
                {/* <Route path="/" element={<Navigate to="/log-in" replace />} /> */}
                {routes.map((route) => (
                    <Route
                        key={route.key}
                        element={<PrivateRoute
                            reqLogin={route.reqLogin}
                            reqSuperUser={route.reqSuperUser}
                        />}
                    >
                        <Route path={route.path} element={route.element} />
                    </Route>
                ))}
            </Routes>
            <ToastContainer
                position="top-right"
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