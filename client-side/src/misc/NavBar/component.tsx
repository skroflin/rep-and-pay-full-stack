import { useNavigate } from "react-router";
import type { RouteElement } from "../../routes";
import { useUser } from "../../user-context/User";
import Sider from "antd/es/layout/Sider";
import { useState } from "react";
import Title from "antd/es/typography/Title";
import { Button, Menu } from "antd";
import { LogoutOutlined, MenuFoldOutlined, MenuUnfoldOutlined, UserOutlined } from "@ant-design/icons";

interface NavBarProps {
    routes: RouteElement[]
}

export default function NavBar({ routes }: NavBarProps) {
    const navigate = useNavigate()
    const { user, logout } = useUser()
    const [collapsed, setCollapsed] = useState(false)

    const toggleCollapsed = () => {
        setCollapsed(!collapsed)
    }

    const onSignOut = () => {
        logout()
        navigate("/log-in")
    }

    const shouldBeVisible = (item: RouteElement) => {
        if (!item.onNavBar) return false
        if (!item.reqLogin && !user.isLoggedIn) return false
        if (!item.reqSuperUser && user.role !== "superuser") return false
        return true
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
        >
            <Title
                level={4}
                style={{ color: "#fff", margin: 0 }}
            >
                {!collapsed && "Rep & Pay"}
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

            {user.isLoggedIn && (
                <Button
                    icon={<LogoutOutlined/>}
                    type="primary"
                    danger
                    block
                    onClick={onSignOut}
                >
                    {!collapsed && "Sign out"}
                </Button>
            )}
        </Sider>
    )
}