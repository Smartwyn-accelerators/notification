import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CstmNotificationContent } from './cstm-notification-content';

describe('CstmNotificationContent', () => {
  let component: CstmNotificationContent;
  let fixture: ComponentFixture<CstmNotificationContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CstmNotificationContent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CstmNotificationContent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
