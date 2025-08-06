import React, { useState } from "react";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";
import { useUser } from "../../user/User";
import { useMutation } from "@tanstack/react-query";
import { registerUser } from "../../utils/api";
import { setAuthToken } from "../../utils/helper";
import type { AxiosError } from "axios";

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

    </>
}