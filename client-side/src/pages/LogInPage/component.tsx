import { useState } from "react"
import { Navigate, useNavigate } from "react-router"
import { useUsername, useUserSetter } from "../../user-context/User"
import { useMutation } from "@tanstack/react-query"
import { loginUser } from "../../utils/api"
import { setAuthToken } from "../../utils/helper"
import { toast } from "react-toastify"
import { AxiosError } from "axios"
import { Button, Form, Input, Layout, Spin } from "antd"

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
            setUser(username, role, true)
            navigate("/")
        },
        onError: (error) => {
            if (error instanceof AxiosError) {
                if (error.response?.status === 401) {
                    toast.error("User does not exist!")
                } else {
                    toast.error(error.message)
                }
            }
        },
    })

    return <>
        {
            isUserLoggedIn ? <Navigate to="/" /> :
                <Layout
                    style={{
                        paddingTop: "4em",
                        paddingBottom: "4em",
                        textAlign: "center",
                        alignItems: "center",
                        justifyContent: "center",
                        display: "flex"
                    }}
                >
                    <Form
                        name="basic"
                        labelCol={{ span: 8 }}
                        wrapperCol={{ span: 16 }}
                        style={{ maxWidth: 400 }}
                        initialValues={{ remember: true }}
                        autoComplete="off"
                    >
                        <Form.Item
                            label="Username"
                            name="username"
                            rules={[{ required: true, message: 'Please input your username!' }]}
                        >
                            <Input
                                value={username}
                                onChange={(e) => setUsername(e.target.value)} />
                        </Form.Item>

                        <Form.Item
                            label="Password"
                            name="password"
                            rules={[{ required: true, message: 'Please input your password!' }]}
                        >
                            <Input.Password
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
                </Layout>
        }
        <Spin
            spinning={signInUser.isPending}
            size="large"
            tip="Logging in..."
        />
    </>
}