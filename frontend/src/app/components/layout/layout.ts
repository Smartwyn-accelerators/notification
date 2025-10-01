import { Component } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTreeModule } from '@angular/material/tree';
import { RouterOutlet } from '@angular/router';
import { NotificationService } from '../../core/services/notification.service';
import { CstmNotificationContent } from '../cstm-notification-content/cstm-notification-content';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatBadgeModule,
    MatDividerModule,
    MatSidenavModule,
    MatTreeModule,
    CstmNotificationContent,
    MatProgressSpinnerModule,
    CommonModule
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class Layout {

  sidenavData: any[] = [
    {
      name: "Home",
      route: "/timesheet/landing",
      iconName: "home",
    }
  ];
  selectedNode: any = null;

  // treeControl = new FlatTreeControl<any>(
  //   (item) => item.level,
  //   (item) => item.expandable
  // );
  username = "User";

  hasChildd6e738adac144 = (_: number, item: any) => item.expandable;
  hasNoContentd6e738adac144 = (_: number, _item: any) => _item.name === "";
  notifications = []
  // notifications = [
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  //   {
  //     message: "Addin approved your ti1 time sheet for leave",
  //     timestamp:new Date
  //   },
  // ];
  subscription: any;

  menu4ea66602f0ea4 = [
    {
      "displayName": "Notifications",
      "children": [
        {
          "classButton": "mat-menu-item-0",
          "displayName": "Notifications",
          "state": "enabled"
        },
        {
          "classButton": "mat-menu-item-1",
          "state": "enabled"
        },
        {
          "classButton": "mat-menu-item-2",
          "state": "enabled"
        },
        {
          "classButton": "mat-menu-item-3",
          "state": "enabled"
        },
        {
          "classButton": "mat-menu-item-4",
          "state": "enabled"
        }
      ]
    }
  ];

  constructor(
    // private permissionService: PermissionService,
    // private authService: AuthenticationService,
    private notificationService: NotificationService,
    private http: HttpClient
    // private userService: UserService
  ) { }

  ngOnInit(): void {

    // this.authService.permissionsChange.subscribe(
    //   res => {
    //     this.initializeData();
    //   }
    // )

  }




  initializeData() {
    this.initializeNotifications();
    // this.setUser();
    // this.fillSidenavData();
  }

  initializeNotifications() {
    this.notificationService
      .connect('https://127.0.0.1:5555/api/notifications/stream')
      .subscribe(
        (data: any) => {
          this.notificationCount++;
          console.log("new notfication", this.notifications);
          this.getNotificationList();
        },
        (error: any) => console.error('SSE error:', error)
      );
    this.getNotificationList();
  }

  // setUser() {
  //   let userid = this.authService.getLoggedinUserId();
  //   this.userService.getUser(userid).subscribe((user) => { this.username = user.emailAddress; });
  // }

  fillSidenavData() {
    // this.addPermissionsBasedData();
    // this.addSchedulerData();
  }

  // addSchedulerData() {
  //   const schedulerData = [];
  //   if (this.permissionService.hasAnyPermissionForCrud("JOBDETAILSENTITY")) {
  //     schedulerData.push({
  //       name: "Jobs",
  //       route: "/timesheet/scheduler/jobs",
  //       iconName: "work",
  //     });
  //     schedulerData.push({
  //       name: "Executing Jobs",
  //       route: "/timesheet/scheduler/executingJobs",
  //       iconName: "offline_bolt",
  //     });
  //     schedulerData.push({
  //       name: "Execution History",
  //       route: "/timesheet/scheduler/executionHistory",
  //       iconName: "history",
  //     });
  //   }
  //   if (this.permissionService.hasAllPermissionForCrud("TRIGGERDETAILSENTITY")) {
  //     schedulerData.push({
  //       name: "Triggers",
  //       route: "/timesheet/scheduler/triggers",
  //       iconName: "control_point_duplicate",
  //     });
  //   }
  //   this.sidenavData = this.sidenavData.concat(schedulerData);
  //   this.datasource.data = this.sidenavData;
  // }

  getNotificationList() {
    setTimeout(() => {
      this.notificationService.getNotificationList().subscribe((res: any) => {
        if (res && res.content.length > 0) {
          this.notifications = res.content.reverse();
        }
      },
        (error => {
          console.log(error)
        }))
    }, 1500)
  }
  notificationCount = 0;
  resetNofificationView() {
    this.notificationCount = 0;
  }
  ngOnDestroy(): void { }

  onNodeSelect(node: any): void {
    this.selectedNode = node;
  }

  private transformNode(item: any, level: number): any {
    return {
      name: item.name,
      iconName: item.iconName,
      route: item.route,
      expandable: !!item.children,
      disabled: false,
      matTreeNodePaddingIndent: level > 1 ? level - 1 : 1,
      matTreeNodePadding: item.matTreeNodePadding || "0px",
      level: level,
    };
  }

  // private addPermissionsBasedData(): void {
  //   this.sidenavData = this.getBasePermissions();
  //   this.datasource.data = this.sidenavData;
  // }

  // private getBasePermissions(): any[] {
  //   const data = [];

  //   if (this.permissionService.hasAnyPermissionForCrud("CUSTOMER")) {
  //     data.push({
  //       name: "Customers",
  //       route: "/timesheet/customers",
  //       iconName: "attribution",
  //     });
  //   }
  //   if (!this.permissionService.hasAnyPermissionForCrud("CUSTOMER") && this.permissionService.hasPermission(PERMISSIONS.PROJECT.READ)) {
  //     data.push({
  //       name: "Projects",
  //       route: "/timesheet/projects",
  //       iconName: "area_chart",
  //     });
  //   }
  //   if (
  //     this.permissionService.hasPermission(PERMISSIONS.TIMESHEET.REVIEW) ||
  //     this.permissionService.hasAnyPermissionForCrud("TIMESHEET")
  //   ) {
  //     data.push({
  //       name: "Timesheet Review",
  //       route: "/timesheet/overview",
  //       iconName: "work_history",
  //     });
  //   }
  //   if (
  //     this.permissionService.hasPermission(PERMISSIONS.TIMESHEET.SUBMIT) ||
  //     this.permissionService.hasAnyPermissionForCrud("TIMESHEET_DETAIL")
  //   ) {
  //     this.replaceOrAdd(data, {
  //       name: "Timesheet Management",
  //       route: "/timesheet/details",
  //       iconName: "work_history",
  //     });
  //   }

  //   if (this.permissionService.hasAllPermissionForCrud("TIMEOFF")) {
  //     data.push({
  //       name: "Timesheet Time Off Management",
  //       route: "/timesheet/timeOff",
  //       iconName: "work_off"
  //     });
  //   }
  //   if (!this.permissionService.hasAnyPermissionForCrud("AUDIT")) {
  //     data.push({
  //       name: "Audit",
  //       route: "/timesheet/audit",
  //       iconName: "fact_check",
  //     });
  //   }

  //   return data;
  // }


  // logout() {
  //   this.authService.logout()
  // }
  private replaceOrAdd(data: any[], newItem: any): void {
    const index = data.findIndex((item) => item.name === "Timesheet Review");
    if (index >= 0) {
      data.splice(index, 1);
    }
    data.push(newItem);
  }
}
