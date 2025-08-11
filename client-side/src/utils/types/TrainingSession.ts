export interface TrainingSessionRequest {
    trainerId: number
    dateTime: Date
    trainingType: string
    trainingLevel: string
}

export interface TrainingSessionResponse {
    id: number,
    trainerFirstName: string,
    trainerLastName: string,
    dateTime: Date,
    trainingType: string,
    trainingLevel: string
}