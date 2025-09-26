import { useLocation, useNavigate } from "react-router";
import type { RouteElement } from "../../routes";
import { isLoggedIn, useRole, useUsername, useUserSetter } from "../../user-context/User";
import Sider from "antd/es/layout/Sider";
import { useState } from "react";
import { Button, Divider, Menu, Typography } from "antd";
import {
    LogoutOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
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
    const username = useUsername()
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
        if (item.reqLogin && !isUserLoggedIn) return false

        const currentRole = (role || "").toLowerCase()
        const allowedRoles = item.allowedRoles.map(r => r.toLowerCase())

        if (allowedRoles.length > 0 && !allowedRoles.includes(currentRole)) {
            return false
        }
        return true
    }

    const { Title, Text } = Typography

    const menuItems = routes
        .filter(shouldBeVisible)
        .map((item) => ({
            key: item.path,
            label: item.key,
            icon: item.icon
        }))

    return (
        <Sider
            collapsible
            collapsed={collapsed}
            trigger={null}
            theme="dark"
            style={{
                borderRadius: 10,
                padding: "6em 0em"
            }}
        >
            <Title
                level={2}
                style={{
                    color: "#fff",
                    margin: 0,
                    fontStyle: "italic"
                }}
            >
                {collapsed ? "R&P" : "Rep & Pay"}
            </Title>
            <Title
                level={5}
                style={{
                    color: "#fff"
                }}
            >
                {collapsed ? "" : `${role}`}
            </Title>
            <Divider style={{ borderColor: "white" }} />
            <Text
                style={{
                    color: "#fff"
                }}
            >
                {collapsed
                    ? `${username?.slice(0, 2)}`
                    : (
                        <Text
                            style={{
                                color: "#fff"
                            }}
                        >
                            <UserOutlined /> {username}
                        </Text>
                    )
                }
            </Text>
            <Divider style={{ borderColor: "white" }} />
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
                <>
                    <Divider style={{ borderColor: "white" }} />
                    <Button
                        icon={<LogoutOutlined />}
                        type="primary"
                        danger
                        onClick={onSignOut}
                    >
                        {!collapsed && "Sign Out!"}
                    </Button>
                </>
            )}
        </Sider>
    )
}