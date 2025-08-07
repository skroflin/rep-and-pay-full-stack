import React, { useState } from "react";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";
import { useUser } from "../../user-context/User";
import { useMutation } from "@tanstack/react-query";
import { registerUser } from "../../utils/api";
import { setAuthToken } from "../../utils/helper";
import type { AxiosError } from "axios";
import { Button, Form, Input, Select, Spin } from "antd";
import { Option } from "antd/es/mentions";

export default function SignUp() {
    const { setUser } = useUser()
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
            toast.error(undefined)
            setAuthToken(response.token)
            if (setUser) {
                setUser(formData)
            }
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

    return <>
        <Form
            name="basic"
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            style={{ maxWidth: 600 }}
            initialValues={{ remember: true }}
            autoComplete="off"
        >
            <Form.Item
                label="First name"
                name="firstName"
                rules={[{ required: true, message: 'Please input your first name!' }]}
            >
                <Input
                    value={formData.firstName}
                    onChange={handleChange} />
            </Form.Item>

            <Form.Item
                label="Last name"
                name="lastName"
                rules={[{ required: true, message: 'Please input your last name/surname!' }]}
            >
                <Input
                    value={formData.lastName}
                    onChange={handleChange} />
            </Form.Item>

            <Form.Item
                label="Username"
                name="username"
                rules={[{ required: true, message: 'Please input your username!' }]}
            >
                <Input
                    value={formData.username}
                    onChange={handleChange} />
            </Form.Item>

            <Form.Item
                label="Email"
                name="email"
                rules={[{ required: true, message: 'Please input your email!' }]}
            >
                <Input
                    value={formData.email}
                    onChange={handleChange} />
            </Form.Item>

            <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: 'Please input your password!' }]}
            >
                <Input.Password
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                />
            </Form.Item>

            <Form.Item
                label="Confirm password"
                name="confirmPassword"
                rules={[{ required: true, message: 'Please confirm your password!' }]}
            >
                <Input.Password
                    type="password"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                />
            </Form.Item>

            <Form.Item name="note" label="Note" rules={[{ required: true }]}>
                <Input />
            </Form.Item>
            <Form.Item name="gender" label="Gender" rules={[{ required: true }]}>
                <Select
                    placeholder="Select a option and change input text above"
                    allowClear
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

            <Button type="link" onClick={() => navigate("/log-in")}>
                Already have an account? Log in!
            </Button>
        </Form>
        <Spin
            spinning={signUpUser.isPending}
            size="large"
            tip="Logging in..."
        />
    </>
}