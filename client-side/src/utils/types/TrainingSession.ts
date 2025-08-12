export interface TrainingSessionRequest {
    trainerId: string,
    dateTime: Date | string,
    trainingType: string,
    trainingLevel: string,
}

export interface TrainingSessionResponse {
    id: string,
    trainerFirstName: string,
    trainerLastName: string,
    dateTime: Date | string,
    trainingType: string,
    trainingLevel: string
}