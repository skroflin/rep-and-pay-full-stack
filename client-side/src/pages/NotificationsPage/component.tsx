import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings } from "../../utils/api";

export default function NotificationsPage() {
    const queryClient = useQueryClient()
    const [selectedBooking, setSelectedBooking] = useState<TrainerBookingResponse | null>(null)

    const { data: bookings, isLoading } = useQuery<TrainerBookingResponse[]>({
        queryKey: ["trainer-bookings"],
        queryFn: getTrainerBookings
    })

    const updateStatusMutation = useMutation({
        mutationFn: ({ bookingId, bookingStatus }: { bookingId: number, bookingStatus: "APPROVED" | "REJECTED" }) => 
            update
    })
}