import { useState } from "react"
import { Navigate, useNavigate } from "react-router"
import { isLoggedIn, useUserSetter } from "../../user-context/User"
import { useMutation } from "@tanstack/react-query"
import { loginUser } from "../../utils/api"
import { setAuthToken, setRole } from "../../utils/helper"
import { toast } from "react-toastify"
import { AxiosError } from "axios"
import { Button, Divider, Flex, Form, Input, Spin, Typography } from "antd"
import { LockOutlined, LoginOutlined, UserOutlined } from "@ant-design/icons"
import LogInLogo from "../../misc/Logo/LogInLogo"
import type { JwtResponse } from "../../utils/types/user/JwtResponse"
import type { LoginUserRequest } from "../../utils/types/user/Login"

export default function LoginIn() {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const navigate = useNavigate()
    const setUser = useUserSetter()
    const userLoggedIn = isLoggedIn()

    const signInUser = useMutation<JwtResponse, AxiosError, LoginUserRequest>({
        mutationFn: loginUser,
        onSuccess: (response) => {
            const { jwt, username, role } = response

            setAuthToken(jwt)
            setUsername(username)
            setRole(role)
            setUser(username, role, true)

            toast.success(`${username} welcome back!`)
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

    const { Title, Text } = Typography

    return <>
        {
            userLoggedIn ? <Navigate to="/" /> :
                <Flex vertical justify="center" align="center" style={{ minHeight: "90vh" }}>
                    <Flex vertical align="center" justify="center">
                        <Flex>
                            <Title level={2}>Log In</Title>
                            <LogInLogo props={{ width: 90, height: 90 }} />
                        </Flex>
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
                            <Divider />
                            <Form.Item>
                                <Text>
                                    Don't have an account?
                                </Text>
                                <Button
                                    style={{
                                        textAlign: "center",
                                        alignItems: "center",
                                        justifyContent: "center",
                                        display: "flex",
                                        width: "100%"
                                    }}
                                    type="link"
                                    onClick={() => navigate("/sign-up")}
                                >
                                    Sign up!
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