import { useState } from "react"
import { Navigate, useNavigate } from "react-router"
import { useUsername, useUserSetter } from "../../user-context/User"
import { useMutation } from "@tanstack/react-query"
import { loginUser } from "../../utils/api"
import { setAuthToken, setRole } from "../../utils/helper"
import { toast } from "react-toastify"
import { AxiosError } from "axios"
import { Button, Flex, Form, Input, Spin, Typography } from "antd"
import { LockOutlined, LoginOutlined, UserOutlined } from "@ant-design/icons"

export default function LoginIn() {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const navigate = useNavigate()
    const setUser = useUserSetter()
    const isUserLoggedIn = useUsername()

    const signInUser = useMutation({
        mutationFn: loginUser,
        onSuccess: (response) => {
            const { jwt, username, role } = response
            toast.success(`${username} welcome back!`)
            setAuthToken(jwt)
            setRole(role)
            setUser(username, role, true)
            navigate("/")
        },
        onError: (error) => {
            if (error instanceof AxiosError) {
                if (error.response?.status === 401) {
                    toast.error("User does not exist!")
                } else if (error.response?.status === 403) {
                    toast.error("Incorrect password or username!")
                } else {
                    toast.error(error.message)
                }
            }
        },
    })

    const { Title } = Typography

    return <>
        {
            isUserLoggedIn ? <Navigate to="/" /> :
                <Flex vertical justify="center" align="center" style={{ minHeight: "90vh" }}>
                    <Flex vertical align="center">
                        <Title level={2} style={{ textAlign: "center" }}>Log In</Title>
                        <Form
                            name="basic"
                            labelCol={{ span: 24 }}
                            wrapperCol={{ span: 24 }}
                            style={{
                                maxWidth: 400,
                                margin: "0 auto",
                                boxShadow: "0 4px 24px rgba(0,0,0,0.08)",
                                borderRadius: 16,
                                border: "1px solid #e4e4e4",
                                padding: 32,
                                background: "#fff",
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center"
                            }}
                            initialValues={{ remember: true }}
                            autoComplete="off"
                        >
                            <Form.Item
                                name="username"
                                rules={[{ required: true, message: 'Please input your username!' }]}
                                style={{ width: "100%" }}
                            >
                                <Input
                                    placeholder="Username"
                                    prefix={<UserOutlined />}
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                />
                            </Form.Item>
                            <Form.Item
                                name="password"
                                rules={[{ required: true, message: 'Please input your password!' }]}
                                style={{ width: "100%" }}
                            >
                                <Input.Password
                                    placeholder="Password"
                                    prefix={<LockOutlined />}
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Form.Item>
                            <Form.Item>
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    onClick={() => signInUser.mutate({
                                        username: username,
                                        password: password
                                    })}
                                    icon={<LoginOutlined />}
                                    style={{ width: "100%" }}
                                >
                                    Log In
                                </Button>
                            </Form.Item>
                            <Form.Item>
                                <Button
                                    type="link"
                                    onClick={() => navigate("/sign-up")}
                                    style={{ width: "100%" }}
                                >
                                    Don't have an account? Sign up!
                                </Button>
                            </Form.Item>
                        </Form>
                    </Flex>
                </Flex>
        }
        <Spin
            spinning={signInUser.isPending}
            size="large"
            tip="Logging in..."
        />
    </>
}