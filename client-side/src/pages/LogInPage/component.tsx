import { useState } from "react"
import { Navigate, useNavigate } from "react-router"
import { useUsername, useUserSetter } from "../../user-context/User"
import { useMutation } from "@tanstack/react-query"
import { loginUser } from "../../utils/api"
import { setAuthToken, setRole } from "../../utils/helper"
import { toast } from "react-toastify"
import { AxiosError } from "axios"
import { Button, Col, Form, Input, Row, Spin } from "antd"
import { LockOutlined, UserOutlined } from "@ant-design/icons"

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

    return <>
        {
            isUserLoggedIn ? <Navigate to="/" /> :
                <Row 
                    justify="center"
                    align="middle"
                    style={{
                        height: "100vh"
                    }}
                >
                    <Col 
                        span={8}
                    >
                        <h1>Log In</h1>
                        <Form
                            name="basic"
                            labelCol={{ span: 8 }}
                            wrapperCol={{ span: 16 }}
                            initialValues={{ remember: true }}
                            autoComplete="off"
                        >
                            <Form.Item
                                name="username"
                                rules={[{ required: true, message: 'Please input your username!' }]}
                            >
                                <Input
                                    placeholder="Username"
                                    prefix={<UserOutlined />}
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)} />
                            </Form.Item>

                            <Form.Item
                                name="password"
                                rules={[{ required: true, message: 'Please input your password!' }]}
                            >
                                <Input.Password
                                    placeholder="Password"
                                    prefix={<LockOutlined />}
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Form.Item>


                            <Form.Item label={null}>
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    onClick={() => signInUser.mutate({
                                        username: username,
                                        password: password
                                    })}
                                >
                                    Log In
                                </Button>
                            </Form.Item>
                            <Button type="link" onClick={() => navigate("/sign-up")}>
                                Don't have an account? Sign up!
                            </Button>
                        </Form>
                    </Col>
                </Row>
        }
        <Spin
            spinning={signInUser.isPending}
            size="large"
            tip="Logging in..."
        />
    </>
}