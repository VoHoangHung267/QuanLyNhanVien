import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, Language } from '../models';
 
@Injectable({ providedIn: 'root' })
export class LanguageService {
  private readonly API = 'http://localhost:8080/api/languages';
  constructor(private http: HttpClient) {}
  getAll(): Observable<ApiResponse<Language[]>> {
    return this.http.get<ApiResponse<Language[]>>(this.API);
  }
}