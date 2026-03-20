import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, Certificate } from '../models';
 
@Injectable({ providedIn: 'root' })
export class CertificateService {
  private readonly API = 'http://localhost:8080/api/certificates';
  constructor(private http: HttpClient) {}
  getAll(): Observable<ApiResponse<Certificate[]>> {
    return this.http.get<ApiResponse<Certificate[]>>(this.API);
  }
}