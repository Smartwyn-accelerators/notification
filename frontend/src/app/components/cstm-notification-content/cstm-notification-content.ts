import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-cstm-notification-content',
  standalone: true,
  imports: [MatIconModule, CommonModule,],
  templateUrl: './cstm-notification-content.html',
  styleUrl: './cstm-notification-content.scss'
})
export class CstmNotificationContent {
  @Input() item: any = undefined;
}
