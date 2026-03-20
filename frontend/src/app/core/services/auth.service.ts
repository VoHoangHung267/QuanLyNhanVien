import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { ApiResponse, LoginRequest, LoginResponse } from '../models';
 
const TOKEN_KEY = 'auth_token';
const USER_KEY  = 'auth_user';
 
@Injectable({ providedIn: 'root' })
export class AuthService {
 
  private readonly API = 'http://localhost:8080/api/auth';
 
  constructor(private http: HttpClient, private router: Router) {}
 
  login(request: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.API}/login`, request).pipe(
      tap(res => {
        if (res.success) {
          localStorage.setItem(TOKEN_KEY, res.data.token);
          localStorage.setItem(USER_KEY, JSON.stringify(res.data));
        }
      })
    );
  }
 
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.router.navigate(['/login']);
  }
 
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }
 
  getCurrentUser(): LoginResponse | null {
    const u = localStorage.getItem(USER_KEY);
    return u ? JSON.parse(u) : null;
  }
 
  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}