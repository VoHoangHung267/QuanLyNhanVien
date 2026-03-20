// src/app/core/models/index.ts

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  username: string;
  role: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface Language {
  id: number;
  name: string;
  level: string;
}

export interface Certificate {
  id: number;
  name: string;
}

export interface Employee {
  id?: number;
  name: string;
  dob: string;
  address: string;
  phone: string;
  languages: Language[];
  certificates: Certificate[];
}

export interface EmployeeRequest {
  name: string;
  dob: string;
  address: string;
  phone: string;
  languageIds: number[];
  certificateIds: number[];
}