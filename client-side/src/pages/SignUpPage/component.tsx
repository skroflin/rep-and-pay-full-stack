import React, { useState } from "react";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";
import { useUserSetter } from "../../user-context/User";
import { useMutation } from "@tanstack/react-query";
import { registerUser } from "../../utils/api";
import { setAuthToken } from "../../utils/helper";
import type { AxiosError } from "axios";
import { Button, Col, Form, Input, Row, Select, Spin } from "antd";
import { Option } from "antd/es/mentions";
import { IdcardFilled, IdcardOutlined, LockFilled, LockOutlined, MailOutlined, TeamOutlined, UserOutlined } from "@ant-design/icons";

export default function SignUp() {
    const setUser = useUserSetter()
    const navigate = useNavigate()
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: ""
    })

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        toast.error(undefined)
        const { name, value } = e.target
        setFormData({
            ...formData,
            [name]: value
        })
    }

    const signUpUser = useMutation({
        mutationFn: registerUser,
        onSuccess: (response) => {
            toast.success(`${formData.username} welcome!`)
            setAuthToken(response.token)
            setUser(formData.username, formData.role, true)
            setTimeout(() => navigate("/"), 1500)
        },
        onError: (error: AxiosError, _variables, _context) => {
            if (error.response?.status === 409) {
                toast.error(`User with username ${formData.username} already exists`)
                toast.error(`User with email ${formData.email} already exists`)
            } else {
                toast.error(`Error upon creating new user. Error: ${error}`)
            }
        }
    })

    const handleSubmit = () => {
        if (!formData.firstName) {
            toast.warning("You must enter your first name!")
        } else if (!formData.lastName) {
            toast.warning("You must enter your last name!")
        } else if (!formData.username) {
            toast.warning("You must enter your username!")
        } else if (!formData.email) {
            toast.warning("You must enter your email!")
        } else if (!formData.password) {
            toast.warning("You must enter your password!")
        } else if (!formData.confirmPassword) {
            toast.warning("You must confirm your password!")
        } else if (formData.password !== formData.confirmPassword) {
            toast.error("Passwords aren't matching!")
        } else if (!formData.role) {
            toast.warning("You must choose a role!")
        } else {
            toast.error(undefined)
            signUpUser.mutate({
                firstName: formData.firstName,
                lastName: formData.lastName,
                username: formData.username,
                email: formData.email,
                password: formData.password,
                role: formData.role
            })
        }
    }

    return (
        <Row justify="center">
            <Col span={8}>
                <h1>Sign Up</h1>
                <Form
                    name="basic"
                    labelCol={{ span: 9 }}
                    wrapperCol={{ span: 16 }}
                    style={{ maxWidth: 400 }}
                    initialValues={{ remember: true }}
                    autoComplete="off"
                >
                    <Form.Item
                        name="firstName"
                        rules={[{ required: true, message: 'Please input your first name!' }]}
                    >
                        <Input
                            placeholder="First name"
                            prefix={<IdcardOutlined />}
                            value={formData.firstName}
                            onChange={handleChange} />
                    </Form.Item>

                    <Form.Item
                        name="lastName"
                        rules={[{ required: true, message: 'Please input your last name/surname!' }]}
                    >
                        <Input
                            placeholder="Last name"
                            prefix={<IdcardFilled />}
                            value={formData.lastName}
                            onChange={handleChange} />
                    </Form.Item>

                    <Form.Item
                        name="username"
                        rules={[{ required: true, message: 'Please input your username!' }]}
                    >
                        <Input
                            placeholder="Username"
                            prefix={<UserOutlined />}
                            value={formData.username}
                            onChange={handleChange} />
                    </Form.Item>

                    <Form.Item
                        name="email"
                        rules={[{ required: true, message: 'Please input your email!' }]}
                    >
                        <Input
                            placeholder="Email"
                            prefix={<MailOutlined />}
                            value={formData.email}
                            onChange={handleChange} />
                    </Form.Item>

                    <Form.Item
                        name="password"
                        rules={[{ required: true, message: 'Please input your password!' }]}
                    >
                        <Input.Password
                            placeholder="Password"
                            prefix={<LockOutlined />}
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </Form.Item>

                    <Form.Item
                        name="confirmPassword"
                        rules={[{ required: true, message: 'Please confirm your password!' }]}
                    >
                        <Input.Password
                            placeholder="Confirm password"
                            prefix={<LockFilled />}
                            type="password"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                        />
                    </Form.Item>

                    <Form.Item name="role" rules={[{ required: true }]}>
                        <Select
                            prefix={<TeamOutlined />}
                            placeholder="Select a role"
                            allowClear
                            onChange={handleChange}
                        >
                            <Option value="user">User</Option>
                            <Option value="coach">Coach</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item label={null}>
                        <Button
                            type="primary"
                            htmlType="submit"
                            onSubmit={handleSubmit}
                        >
                            Sign Up
                        </Button>
                    </Form.Item>

                    <Button
                        style={{
                            textAlign: "center",
                            alignItems: "center",
                            justifyContent: "center"
                        }}
                        type="link"
                        onClick={() => navigate("/log-in")}
                    >
                        Already have an account? Log in!
                    </Button>
                </Form>
                <Spin
                    spinning={signUpUser.isPending}
                    size="large"
                    tip="Logging in..."
                />
            </Col>
        </Row>
    )
}