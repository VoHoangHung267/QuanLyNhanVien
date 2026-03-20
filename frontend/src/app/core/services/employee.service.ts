import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, Employee, EmployeeRequest } from '../models';
 
@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private readonly API = 'http://localhost:8080/api/employees';
 
  constructor(private http: HttpClient) {}
 
  getAll(): Observable<ApiResponse<Employee[]>> {
    return this.http.get<ApiResponse<Employee[]>>(this.API);
  }
 
  getById(id: number): Observable<ApiResponse<Employee>> {
    return this.http.get<ApiResponse<Employee>>(`${this.API}/${id}`);
  }
 
  create(request: EmployeeRequest): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(this.API, request);
  }
 
  update(id: number, request: EmployeeRequest): Observable<ApiResponse<Employee>> {
    return this.http.put<ApiResponse<Employee>>(`${this.API}/${id}`, request);
  }
 
  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API}/${id}`);
  }
}
 