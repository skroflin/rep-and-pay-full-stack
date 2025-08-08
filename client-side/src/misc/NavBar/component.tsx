import { useLocation, useNavigate } from "react-router";
import type { RouteElement } from "../../routes";
import { isLoggedIn, useRole, useUserSetter } from "../../user-context/User";
import Sider from "antd/es/layout/Sider";
import { useState } from "react";
import Title from "antd/es/typography/Title";
import { Button, Divider, Menu } from "antd";
import {
    LogoutOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    ToolOutlined,
    UserOutlined
} from "@ant-design/icons";
import { clearAuthToken } from "../../utils/helper";

interface NavBarProps {
    routes: RouteElement[]
}

export default function NavBar({ routes }: NavBarProps) {
    const navigate = useNavigate()
    const isUserLoggedIn = isLoggedIn()
    const location = useLocation()
    const setUser = useUserSetter()
    const role = useRole()
    const [collapsed, setCollapsed] = useState(false)

    const toggleCollapsed = () => {
        setCollapsed(!collapsed)
    }

    const onSignOut = () => {
        setUser(undefined, undefined, false)
        clearAuthToken()
        navigate("/")
    }

    const shouldBeVisible = (item: RouteElement) => {
        if (!item.onNavBar) return false
        if (!item.reqLogin) return false
        if (!item.allowedRoles) return true
        return item.allowedRoles.includes(role || "")
    }

    const menuItems = routes
        .filter(shouldBeVisible)
        .map((item) => ({
            key: item.path,
            label: item.key,
            icon: <UserOutlined />
        }))

    return (
        <Sider
            collapsible
            collapsed={collapsed}
            trigger={null}
            theme="dark"
            style={{
                padding: "6em 0em"
            }}
        >
            <Title
                level={4}
                style={{
                    color: "#fff",
                    margin: 0
                }}
            >
                {!collapsed && "Rep & Pay "}
                {role === "superuser" && (<ToolOutlined />) || (<ToolOutlined />)}
            </Title>
            <Button
                type="text"
                icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                onClick={toggleCollapsed}
                style={{ color: "white" }}
            />

            <Menu
                theme="dark"
                mode="inline"
                selectedKeys={[location.pathname]}
                onClick={(item) => navigate(item.key)}
                items={menuItems}
            />

            {isUserLoggedIn && (
                <Divider
                    style={{
                        color: "white"
                    }}
                >
                    <Button
                        icon={<LogoutOutlined />}
                        type="primary"
                        danger
                        onClick={onSignOut}
                    >
                        {!collapsed && "Sign Out!"}
                    </Button>
                </Divider>
            )}
        </Sider>
    )
}