import React, { useState } from "react";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";
import { useMutation } from "@tanstack/react-query";
import { registerUser } from "../../utils/api";
import type { AxiosError } from "axios";
import { Button, Form, Input, Flex, Select, Spin, Typography, Divider } from "antd";
import {
    HourglassOutlined,
    IdcardFilled,
    IdcardOutlined,
    LockFilled,
    LockOutlined,
    MailOutlined,
    TeamOutlined,
    UserAddOutlined,
    UserOutlined
} from "@ant-design/icons";
import SignUpLogo from "../../misc/Logo/SignUpLogo";

export default function SignUp() {
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
        onSuccess: () => {
            toast.success(`${formData.username} registered successfully!`)
            setTimeout(() => navigate("/log-in"), 1000)
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

    const { Title, Text } = Typography

    return (
        <Spin
            indicator={<HourglassOutlined style={{ color: "black", fontSize: 48 }} spin />}
            spinning={signUpUser.isPending}
            style={{ fontSize: 64 }}
            tip={
                <Title level={3} style={{ textAlign: "center", marginTop: 50 }}>
                    Signing user {formData.username} up...
                </Title>
            }
        >
            <Flex
                vertical
                justify="center"
                align="center"
                style={{ minHeight: "90vh" }}
            >
                <Flex vertical align="center">
                    <Flex align="center" justify="center">
                        <Flex>
                            <Title level={2}>
                                Sign Up
                            </Title>
                            <SignUpLogo props={{ width: 90, height: 90 }} />
                        </Flex>
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
                            rules={[{ required: true, message: 'Please input your first name!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input
                                name="firstName"
                                placeholder="First name"
                                prefix={<IdcardOutlined />}
                                value={formData.firstName}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item
                            rules={[{ required: true, message: 'Please input your last name/surname!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input
                                name="lastName"
                                placeholder="Last name"
                                prefix={<IdcardFilled />}
                                value={formData.lastName}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item
                            rules={[{ required: true, message: 'Please input your username!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input
                                name="username"
                                placeholder="Username"
                                prefix={<UserOutlined />}
                                value={formData.username}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item
                            rules={[{ required: true, message: 'Please input your email!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input
                                name="email"
                                placeholder="Email"
                                prefix={<MailOutlined />}
                                value={formData.email}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item
                            rules={[{ required: true, message: 'Please input your password!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input.Password
                                name="password"
                                placeholder="Password"
                                prefix={<LockOutlined />}
                                type="password"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item
                            rules={[{ required: true, message: 'Please confirm your password!' }]}
                            style={{ width: "100%" }}
                        >
                            <Input.Password
                                name="confirmPassword"
                                placeholder="Confirm password"
                                prefix={<LockFilled />}
                                type="password"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item rules={[{ required: true }]} style={{ width: "100%" }}>
                            <Select
                                prefix={<TeamOutlined />}
                                placeholder="Select a role"
                                allowClear
                                onChange={(value) => setFormData({ ...formData, role: value })}
                            >
                                <Select.Option value="user" name="role">User</Select.Option>
                                <Select.Option value="coach" name="role">Coach</Select.Option>
                            </Select>
                        </Form.Item>
                        <Form.Item label={null} style={{ width: "100%", marginTop: 16 }}>
                            <Button
                                style={{
                                    alignItems: "center",
                                    justifyContent: "center",
                                    display: "flex",
                                    width: "100%"
                                }}
                                type="primary"
                                htmlType="submit"
                                onClick={handleSubmit}
                                icon={<UserAddOutlined />}
                            >
                                Sign Up
                            </Button>
                        </Form.Item>
                        <Divider style={{ margin: 0 }} />
                        <Form.Item style={{ width: "100%" }}>
                            <Text>
                                Already have an account?
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
                                onClick={() => navigate("/log-in")}
                            >
                                Log in!
                            </Button>
                        </Form.Item>
                    </Form>
                </Flex>
            </Flex>
        </Spin>
    )
}