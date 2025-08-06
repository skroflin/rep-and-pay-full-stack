import { useNavigate } from "react-router";
import type { RouteElement } from "../../routes";
import { useUser } from "../../user/User";

interface NavBarProps {
    routes: RouteElement[]
}

export default function NavBar({ routes }: NavBarProps) {
    const navigate = useNavigate()
    const { user, logout } = useUser()
    const onSignOut = () => {
        logout()
        navigate("/")
    }

    const isButtonActive = (path: string) => {
        return location.pathname === path
    }

    const shouldBeVisible = (item: RouteElement) => {
        if (!item.onNavBar) return false;
        if (!item.reqLogin) return false;
    }
}