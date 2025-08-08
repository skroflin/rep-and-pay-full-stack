import { QueryClient, useMutation, useQueryClient } from "@tanstack/react-query";
import { Calendar } from "antd";
import { use, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { createMyBooking, getCoaches } from "../../utils/api";

export default function BookingPage() {
    const [coaches, setCoaches] = useState([])
    const [selectedDate, setSelectedDate] = useState<any>(null)
    const [selectedCoach, setSelectedCoach] = useState<string | null>(null)
    const [startTime, setStartTime] = useState<any>(null)
    const [endTime, setEndTime] = useState<any>(null)
    const [modalOpen, setModalOpen] = useState(false)

    const queryClient = useQueryClient()

    const createBookingMutation = useMutation({
        mutationFn: (req: any) => createMyBooking(req),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["myBookings"] })
            toast.success("Booking request sent!")
            setModalOpen(false)
        },
        onError: () => {
            toast.error("Error upon sending request!")
        }
    })

    useEffect(() => {
        getCoaches().then(setCoaches)
    }, [])

    const handleDateSelect = (value: any) => {
        setSelectedDate(value)
        setModalOpen(true)
    }

    return (
        <div
            style={{
                display: "block",
                width: 700,
                padding: 30
            }}
        >
            <Calendar
                onChange={(value) => {
                    toast.success(`You selected ${value.format('DD.MM.YYYY')}`)
                }} 
            />
        </div>
    )
}