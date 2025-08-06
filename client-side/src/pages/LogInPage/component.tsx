import { useState } from "react"
import { Navigate, useNavigate } from "react-router"
import { useUser } from "../../user/User"
import { useMutation } from "@tanstack/react-query"
import { loginUser } from "../../utils/api"
import { setAuthToken } from "../../utils/helper"
import { toast } from "react-toastify"
import { AxiosError } from "axios"
import { Button, Form, Input, Spin } from "antd"

export default function LoginIn() {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const navigate = useNavigate()
    const { user, setUser } = useUser()

    const signInUser = useMutation({
        mutationFn: loginUser,
        onSuccess: (response) => {
            const { token } = response
            toast.error(undefined)
            setAuthToken(token)
            if (setUser) {
                setUser({ username, isLoggedIn: true })
            }

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
            user.isLoggedIn ? <Navigate to="/" /> :
                <Form
                    name="basic"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    style={{ maxWidth: 600 }}
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
                            type="primary" htmlType="submit"
                            onClick={() => signInUser.mutate({ 
                                username: username, 
                                password: password 
                            })}
                        >
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
        }
        <Spin
            spinning={signInUser.isPending}
            size="large"
            tip="Logging in..."
        />
    </>
}