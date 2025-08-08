import { 
    Navigate,
    Outlet, 
    Route, 
    Routes
} from "react-router"
import { 
    isLoggedIn,
    useRole
} from "./user-context/User"
import { Layout } from "antd"
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import LoginIn from "./pages/LogInPage/component";
import SignUp from "./pages/SignUpPage/component";
import NavBar from "./misc/NavBar/component";
import HomePage from "./pages/HomePage/component";
import CoachPage from "./pages/CoachPage/component";
import UserPage from "./pages/UserPage/component";
import BookingPage from "./pages/BookingPage/component";
import { 
    CalendarOutlined,
    HomeOutlined,
    TeamOutlined,
    UserOutlined 
} from "@ant-design/icons";
import TrainingSessionPage from "./pages/TrainingSessionPage/component";

export interface RouteElement {
    key: string
    path: string
    element: React.JSX.Element
    onNavBar: boolean
    reqLogin: boolean,
    allowedRoles: string[],
    icon?: React.ReactNode
}

const routes: RouteElement[] = [
    {
        key: "Sign Up",
        path: "/sign-up",
        element: <SignUp />,
        onNavBar: false,
        reqLogin: false,
        allowedRoles: ["user", "superuser", "coach"]
    },
    {
        key: "Log In",
        path: "/log-in",
        element: <LoginIn />,
        onNavBar: false,
        reqLogin: false,
        allowedRoles: ["user", "superuser", "coach"]
    },
    {
        key: "Home",
        path: "/",
        element: <HomePage />,
        onNavBar: true,
        reqLogin: true,
        allowedRoles: ["user", "superuser", "coach"],
        icon: <HomeOutlined />
    },
    {
        key: "Coaches",
        path: "/coaches",
        element: <CoachPage />,
        onNavBar: true,
        reqLogin: true,
        allowedRoles: ["superuser"],
        icon: <TeamOutlined />
    },
    {
        key: "Users",
        path: "/users",
        element: <UserPage />,
        onNavBar: true,
        reqLogin: true,
        allowedRoles: ["superuser"],
        icon: <UserOutlined />
    },
    {
        key: "Bookings",
        path: "/bookings",
        element: <BookingPage />,
        onNavBar: true,
        reqLogin: true,
        allowedRoles: ["user"],
        icon: <CalendarOutlined />
    },
    {
       key: "Training Session",
        path: "/training-sessions",
        element: <TrainingSessionPage />,
        onNavBar: true,
        reqLogin: true,
        allowedRoles: ["coach"],
        icon: <CalendarOutlined /> 
    }
]

interface PrivateRouteProps {
    reqLogin: boolean,
    allowedRoles: string[]
}

function PrivateRoute({ reqLogin, allowedRoles }: PrivateRouteProps) {
    const isUserLoggedIn = isLoggedIn()
    const role = useRole()

    if (reqLogin && !isUserLoggedIn) {
        return <Navigate to="/log-in" />
    }

    if(
        isUserLoggedIn && allowedRoles.length > 0 
        && !allowedRoles.includes(role || "")
    ) {
        return <Navigate to="/"/>
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
                {routes.map((route) => (
                    <Route
                        key={route.key}
                        element={<PrivateRoute
                            reqLogin={route.reqLogin}
                            allowedRoles={route.allowedRoles}
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