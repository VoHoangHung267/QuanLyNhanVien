import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
 
export const routes: Routes = [
  { path: '', redirectTo: 'employees', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'employees',
    canActivate: [AuthGuard],
    loadComponent: () => import('./features/employees/employee-list.component').then(m => m.EmployeeListComponent)
  },
  {
    path: 'employees/new',
    canActivate: [AuthGuard],
    loadComponent: () => import('./features/employees/employee-form.component').then(m => m.EmployeeFormComponent)
  },
  {
    path: 'employees/:id/edit',
    canActivate: [AuthGuard],
    loadComponent: () => import('./features/employees/employee-form.component').then(m => m.EmployeeFormComponent)
  },
  { path: '**', redirectTo: 'employees' }
];
 

