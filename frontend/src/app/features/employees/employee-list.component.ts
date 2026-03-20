import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule} from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { EmployeeService } from '../../core/services/employee.service';
import { AuthService } from '../../core/services/auth.service';
import { Employee } from '../../core/models';
 
@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  loading = false;
  errorMessage = '';
  deleteId: number | null = null;
  get currentUser() {
  return this.authService.getCurrentUser();
}
 
  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}
 
  ngOnInit(): void { this.loadEmployees(); }
 
  loadEmployees(): void {
    this.loading = true;
    this.cdr.detectChanges(); // ← thêm: báo Angular cập nhật UI ngay

    this.employeeService.getAll().subscribe({
      next: (res) => {
        this.employees = res.data || [];
        this.loading = false;
        this.cdr.detectChanges(); // ← thêm: báo Angular cập nhật UI
      },
      error: () => {
        this.errorMessage = 'Không thể tải danh sách nhân viên';
        this.loading = false;
        this.cdr.detectChanges(); // ← thêm
      }
    });
  }
 
  confirmDelete(id: number): void { this.deleteId = id; }
  cancelDelete(): void { this.deleteId = null; }
 
  doDelete(): void {
    if (this.deleteId == null) return;
    this.employeeService.delete(this.deleteId).subscribe({
      next: () => { this.deleteId = null; this.loadEmployees(); },
      error: () => { this.errorMessage = 'Xóa thất bại'; this.deleteId = null; }
    });
  }
 
  formatDate(dob: string): string {
  if (!dob) return '';
  const [year, month, day] = dob.split('-');
  return `${day}/${month}/${year}`;
}
  logout(): void { this.authService.logout(); }
}